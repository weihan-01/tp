---
  layout: default.md
  title: "Jo Shin's Project Portfolio Page"
---

### Project: Neighbourly

Neighbourly is a **desktop app for managing senior and caregiver contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Neighbourly can get your contact management tasks done faster than traditional GUI apps.

Given below are my [contributions](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=f13-4&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=j-os-h-in&tabRepo=AY2526S1-CS2103-F13-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false) to the project.

### Overview

I focused on the overall domain modelling, architecture design and acceptance testing of the project, aligning code behaviour and GUI design with the senior–caregiver domain.

### Enhancements / Contributions

### Project Management / Team-based tasks

- Maintained project workflow with weekly scrum meetings, issue identification and task allocation.
- Ensure project deliverables are done on time and in the right format.

#### UI Component

- Designed the `SeniorCard` and `CaregiverCard`
- Separated persons into two different list views for seniors and caregivers for greater usability

#### Logic Component

- Worked on the `filter`, `unassign`, `delete`, `pin` and `unpin` commands

#### Model Component

- Followed OOP principles to create a generic class `UniquePersonsList` for various types of stakeholders in the senior-caregiving context
- Added `Senior` and `Caregiver` classes as subclasses of abstract class `Person` with unique attributes to each

### Contributions to the User Guide (UG)

- Completed the original draft of the MVP with command tokens and outputs for use in the User Guide
- Revamped user guide to Neighbourly's unique website

### Contributions to the Developer Guide (DG)

- Updated Model and UI class diagrams to reflect Neighbourly's use case

### Community

- Code reviews: Tested and reviewed parser/command PRs (focus on edge cases, error messages, and failure-path coverage).
- The main challenge was reconciling legacy AB3 semantics with our senior–caregiver domain: different optionality rules (caregiver address optional vs. senior mandatory), multiple index-bearing prefixes (`s/`, `c/`), and nuanced duplicate-prefix handling. The final test suite captures these branches and locks in the expected behavior, making future refactors safer.
