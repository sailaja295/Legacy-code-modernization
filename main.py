from legacy_ai.agents.orchestrator import run

JAVA_FILE = "legacy_code/DemoApplication.java"

with open(JAVA_FILE, encoding="utf-8") as f:
    java_code = f.read()

run(java_code)
