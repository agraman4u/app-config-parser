# App Config Parser - Python

A centralized multi-stage and multi-region configuration parser using wildcard entries.

## Installation

```bash
pip install app-config-parser
```

## Setup

1. Create configuration directory:
```bash
mkdir -p configuration/app-config
```

2. Create configuration files with format `<name>.APPLICATION_NAME.conf`:

```
<stage>.<region>.<identifier> += {
    "<key1>": "<value1>",
    "<key2>": "<value2>"
}
```

## Usage

```python
from app_config_parser import AppConfig

# Initialize
AppConfig.init_app_config("MyApp", "prod", "us-west-2")

# Get values
name = AppConfig.get("databaseConfig", "name")
port = AppConfig.get("databaseConfig", "port", int)

# Get all entries
config = AppConfig.get_all_entries("databaseConfig")
```

## Environment Variables

- `DEPLOYMENT_STAGE`: Default stage (default: "dev")
- `DEPLOYMENT_REGION`: Default region (default: "local")