def is_wildcard_or_val(pattern: str, value: str) -> bool:
    return pattern in ["*", value]

def is_wildcard(pattern: str) -> bool:
    return pattern == "*"