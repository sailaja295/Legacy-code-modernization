from openai import AzureOpenAI
import os
from dotenv import load_dotenv

load_dotenv(".env")   # 👈 force load

client = AzureOpenAI(
    api_key=os.getenv("AZURE_OPENAI_API_KEY"),
    azure_endpoint=os.getenv("AZURE_OPENAI_ENDPOINT"),
    api_version="2024-12-01-preview"
)

def ask_llm(prompt):
    response = client.chat.completions.create(
        model=os.getenv("AZURE_OPENAI_DEPLOYMENT"),
        messages=[
            {"role": "system", "content": "You are a senior software architect."},
            {"role": "user", "content": prompt}
        ]
    )
    return response.choices[0].message.content
