import re
import os
from pathlib import Path
from typing import List, Dict, Optional, Tuple
from app_config_parser.parser.parser import Parser
from app_config_parser.utils.types import AppConfigurationArgs, ConfigEntry, AppConfigMap
from app_config_parser.utils.strings import is_wildcard_or_val, is_wildcard
from app_config_parser.utils.files import list_files, read_config_file

class AppConfigParser(Parser):
    def __init__(self, configuration_args: AppConfigurationArgs):
        self.configuration_args = configuration_args
        self.root_config_dir = Path(os.getcwd()) / "configuration" / "app-config"
        
        pattern = re.compile(f".*\\.{configuration_args.service_name}\\.conf")
        self.config_files = [f for f in list_files(self.root_config_dir) if pattern.match(f.name)]
        self.values = self.parse()
    
    def parse(self) -> AppConfigMap:
        all_file_mappings = []
        for config_file in self.config_files:
            all_file_mappings.extend(self._parse_file(config_file))
        
        filtered_mappings = [
            entry for entry in all_file_mappings
            if is_wildcard_or_val(entry.stage, self.configuration_args.stage)
            and is_wildcard_or_val(entry.region, self.configuration_args.region)
        ]
        
        return self._build_app_config(filtered_mappings)
    
    def get_value(self, identifier: str, key: str) -> Optional[str]:
        return self.values.get(identifier, {}).get(key)
    
    def get_all_entries(self, identifier: str) -> Optional[Dict[str, str]]:
        return self.values.get(identifier)
    
    def _parse_file(self, path: Path) -> List[ConfigEntry]:
        config_lines = read_config_file(path)
        entries = []
        current_entry = None
        properties_lines = []
        
        for line in config_lines:
            header_match = re.match(r'^(\w+|\*)\.(\w+|\*)\.(\w+|\*)\s*\+=\s*\{\s*$', line)
            if header_match:
                if current_entry:
                    properties_str = ' '.join(properties_lines)
                    entries.append(ConfigEntry(current_entry[0], current_entry[1], current_entry[2], properties_str))
                
                stage, region, identifier = header_match.groups()
                current_entry = (stage, region, identifier)
                properties_lines = []
            elif line == '}':
                if current_entry:
                    properties_str = ' '.join(properties_lines)
                    entries.append(ConfigEntry(current_entry[0], current_entry[1], current_entry[2], properties_str))
                    current_entry = None
                    properties_lines = []
            elif current_entry and line.strip():
                properties_lines.append(line.rstrip(','))
        
        return entries
    
    def _build_app_config(self, entries: List[ConfigEntry]) -> AppConfigMap:
        app_config_map: AppConfigMap = {}
        
        def update_app_config(entry: ConfigEntry):
            identifier, mappings = self._get_identifier_map(entry)
            self._update_map(app_config_map, identifier, mappings)
        
        generics = [e for e in entries if is_wildcard(e.stage) and is_wildcard(e.region)]
        for entry in generics:
            update_app_config(entry)
        
        specific_entries = [
            e for e in entries
            if not (is_wildcard(e.stage) and is_wildcard(e.region))
            and (is_wildcard(e.stage) or is_wildcard(e.region))
        ]
        for entry in specific_entries:
            update_app_config(entry)
        
        exact_entries = [e for e in entries if not is_wildcard(e.stage) and not is_wildcard(e.region)]
        for entry in exact_entries:
            update_app_config(entry)
        
        return app_config_map
    
    def _get_identifier_map(self, entry: ConfigEntry) -> Tuple[str, Dict[str, str]]:
        properties_map = {}
        pattern = re.compile(r'\s*"(\w+)"\s*:\s*"([^"]+)"\s*')
        
        for match in pattern.finditer(entry.mappings):
            key, value = match.groups()
            properties_map[key] = value
        
        return entry.identifier, properties_map
    
    def _update_map(self, app_config_map: AppConfigMap, identifier: str, mappings: Dict[str, str]):
        if identifier not in app_config_map:
            app_config_map[identifier] = {}
        
        for key, value in mappings.items():
            app_config_map[identifier][key] = value