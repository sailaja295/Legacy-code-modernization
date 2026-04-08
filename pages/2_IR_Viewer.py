import streamlit as st
import json
import os

IR_PATH = "ir/system_ir.json"

st.set_page_config(page_title="IR Viewer", layout="wide")
st.title("🧩 Intermediate Representation (IR)")

if not os.path.exists(IR_PATH):
    st.warning("IR not found.")
else:
    with open(IR_PATH, encoding="utf-8") as f:
        ir = json.load(f)

    st.download_button(
        "⬇ Download IR",
        json.dumps(ir, indent=2),
        file_name="system_ir.json"
    )

    st.markdown("---")
    st.json(ir)
