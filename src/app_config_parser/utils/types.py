from dataclasses import dataclass
from typing import MutableMapping

@dataclass
class AppConfigurationArgs:
    stage: str
    region: str
    service_name: str

@dataclass
class ConfigEntry:
    stage: str
    region: str
    identifier: str
    mappings: str

AppConfigMap = MutableMapping[str, MutableMapping[str, str]]