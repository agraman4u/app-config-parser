from abc import ABC, abstractmethod
from typing import Dict, Optional
from app_config_parser.utils.types import AppConfigMap

class Parser(ABC):
    @abstractmethod
    def parse(self) -> AppConfigMap:
        pass
    
    @abstractmethod
    def get_value(self, identifier: str, key: str) -> Optional[str]:
        pass
    
    @abstractmethod
    def get_all_entries(self, identifier: str) -> Optional[Dict[str, str]]:
        pass