import streamlit as st
import json
from legacy_ai.agents.orchestrator import run

st.title("Agentic AI – Legacy Modernization Platform")

uploaded_file = st.file_uploader("Upload Legacy Java File", type="java")

if uploaded_file:
    java_code = uploaded_file.read().decode("utf-8")

    if st.button("Run AI Modernization"):
        run(java_code)

    with open("ir/system_ir.json") as f:
        ir = json.load(f)

    st.subheader("System IR")
    st.json(ir)

    if "documentation" in ir:
        st.subheader("Generated Documentation")
        st.text(ir["documentation"])

    if "doc_critique" in ir:
        st.subheader("Critic Feedback")
        st.text(ir["doc_critique"])
