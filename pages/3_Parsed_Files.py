import streamlit as st
import os
from pathlib import Path

CHUNKS_DIR = "chunks"

st.set_page_config(page_title="Parsed Java Files", layout="wide")
st.title("📂 Parsed Java Chunks")

if not os.path.exists(CHUNKS_DIR):
    st.warning("No parsed chunks found.")
else:
    files = sorted(Path(CHUNKS_DIR).glob("*.java"))
    st.write(f"Total chunks: {len(files)}")

    for f in files:
        with st.expander(f.name):
            st.code(f.read_text(errors="ignore"), language="java")
