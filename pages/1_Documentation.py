import streamlit as st
import os

DOC_PATH = "docs/system_design.md"

st.set_page_config(page_title="Documentation", layout="wide")
st.title("📘 System Design Documentation")

if not os.path.exists(DOC_PATH):
    st.warning("Documentation not found.")
else:
    with open(DOC_PATH, encoding="utf-8") as f:
        doc = f.read()

    st.download_button(
        "⬇ Download Documentation",
        doc,
        file_name="system_design.md"
    )

    st.markdown("---")
    st.markdown(doc)
