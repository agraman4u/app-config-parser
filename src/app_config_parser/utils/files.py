from pathlib import Path
from typing import List

def list_files(directory: Path) -> List[Path]:
    if not directory.exists():
        return []
    return [f for f in directory.iterdir() if f.is_file()]

def read_config_file(path: Path) -> List[str]:
    with open(path, 'r') as f:
        return [line.strip() for line in f.readlines() if line.strip()]