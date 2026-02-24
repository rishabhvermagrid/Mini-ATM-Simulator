# ATM Simulator (Core Java)

## Overview

Design and implement a console-based ATM Simulator using **Core Java only**.

The system:

* Simulates real ATM banking operations
* Follows clean architecture principles
* Persists account data using CSV files
* Demonstrates proper use of Core Java concepts
* Uses **no external frameworks or third-party libraries**

---

## ATM Operations

After successful login, the user can:

1. Check Balance
2. Deposit
3. Withdraw
4. Transfer
5. View Transaction History
6. Exit

---

## Data Persistence

* Account data is stored in a CSV file.
* Transaction history is stored in a separate CSV file.
* Account history and transaction history are persisted separately.

---

## Authentication

User must log in using:

* Account Number
* PIN

### System Behavior

* Incorrect PIN increments failed attempts.
* After **3 incorrect attempts**, the account is locked.
* A locked account cannot perform any operations.
