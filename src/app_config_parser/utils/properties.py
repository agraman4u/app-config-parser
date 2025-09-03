import os

def get_stage() -> str:
    return os.environ.get("DEPLOYMENT_STAGE", "dev")

def get_region() -> str:
    return os.environ.get("DEPLOYMENT_REGION", "local")