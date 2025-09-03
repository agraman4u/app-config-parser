import pytest
import os
import sys
from pathlib import Path

# Add src to path for testing
sys.path.insert(0, str(Path(__file__).parent.parent / "src"))

from app_config_parser import AppConfig

class TestAppConfig:
    def setup_method(self):
        AppConfig._app_config = None
    
    def test_init_and_get_values(self):
        os.environ["DEPLOYMENT_STAGE"] = "prod"
        os.environ["DEPLOYMENT_REGION"] = "WestUs2"
        
        AppConfig.init_app_config("AppConfigTest")
        
        name = AppConfig.get("databaseDriverConfig", "name")
        assert name == "hello world prod west us"
        
        id_val = AppConfig.get("databaseDriverConfig", "id", int)
        assert id_val == 42
    
    def test_wildcard_matching(self):
        os.environ["DEPLOYMENT_STAGE"] = "dev"
        os.environ["DEPLOYMENT_REGION"] = "local"
        
        AppConfig.init_app_config("AppConfigTest")
        
        name = AppConfig.get("databaseDriverConfig", "name")
        assert name == "hello world"
    
    def test_get_all_entries(self):
        os.environ["DEPLOYMENT_STAGE"] = "prod"
        os.environ["DEPLOYMENT_REGION"] = "EastUs1"
        
        AppConfig.init_app_config("AppConfigTest")
        
        all_entries = AppConfig.get_all_entries("databaseDriverConfig")
        assert all_entries["name"] == "hello world prod.EastUs1"