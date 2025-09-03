from typing import TypeVar, Dict, Optional, Type
from app_config_parser.parser.app_config_parser import AppConfigParser
from app_config_parser.parser.parser import Parser
from app_config_parser.utils.types import AppConfigurationArgs
from app_config_parser.utils.properties import get_stage, get_region

T = TypeVar('T')

class AppConfig:
    _app_config: Optional['AppConfig'] = None
    
    def __init__(self, args: AppConfigurationArgs):
        self.parser: Parser = AppConfigParser(args)
    
    @classmethod
    def init_app_config(cls, service_name: str, stage: str = None, region: str = None):
        if stage is None:
            stage = get_stage()
        if region is None:
            region = get_region()
        
        cls._app_config = cls(AppConfigurationArgs(stage, region, service_name))
    
    @classmethod
    def get(cls, identifier: str, key: str, return_type: Type[T] = str) -> T:
        if cls._app_config is None:
            raise RuntimeError("AppConfig not initialized. Call init_app_config() first.")
        
        value = cls._app_config.parser.get_value(identifier, key)
        if value is None:
            raise KeyError(f"Key '{key}' not found in identifier '{identifier}'")
        
        if return_type == str:
            return value
        elif return_type == int:
            return int(value)
        elif return_type == float:
            return float(value)
        elif return_type == bool:
            return value.lower() in ('true', '1', 'yes', 'on')
        else:
            raise ValueError(f"Unsupported type: {return_type}")
    
    @classmethod
    def get_all_entries(cls, identifier: str) -> Dict[str, str]:
        if cls._app_config is None:
            raise RuntimeError("AppConfig not initialized. Call init_app_config() first.")
        
        return cls._app_config.parser.get_all_entries(identifier) or {}