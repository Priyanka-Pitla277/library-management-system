
# Playbook: Executing Dynamic Sandbox Logistics Transit

This execution guide details the steps to run, trace, and debug the console-driven sandbox transfer routine inside the Library Management System.

---

## 🔄 Lifecycle Data State Machine

When you run this method, the transient memory structure updates the book asset state using a step-by-step state machine:

```mermaid
stateDiagram-v2
    [*] --> ReadConsoleInput : User inputs IDs
    ReadConsoleInput --> SandboxInit : Instantiates branch1, branch2 & BookCopy
    SandboxInit --> AvailableAtSource : addCopy() called (Status: AVAILABLE)
    AvailableAtSource --> InTransit : initiateTransit() (Status: IN_TRANSIT)
    InTransit --> TransferredToTarget : receiveTransit() (Status: AVAILABLE)
    TransferredToTarget --> [*] : Complete