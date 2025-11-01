---
  layout: default.md
  title: "Grace's Project Portfolio Page"
---


### Project: Neighbourly

Neighbourly is a **desktop app for managing senior and caregiver contacts, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Neighbourly can get your contact management tasks done faster than traditional GUI apps.

Given below are my contributions to the project.

### New Features
* `add-snr` — **Add Senior**
    * What it does: Creates a Senior with risk tag (High Risk/HR, Medium Risk/MR, Low Risk/LR), phone, address, and optional note. Risk is persisted and rendered as a colored chip.
    * Justification: Seniors are core domain entities; risk tagging is essential for triage and filtering.
    * Highlights: Designed a clean parser/command pair (`AddSeniorCommand`, `AddSeniorCommandParser`) and updated storage adapters/tests for the new role and risk schema.
    * Credits: Referenced the AB3 original `Person.java` and `AddCommandParser` file to maintain code consistency.
    * Issues/PRs: Fix [\#41](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/41) PR [\#42](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/42)


* `add-cgr` — **Add Caregiver with Auto-ID**
    * What it does: Creates a Caregiver with phone/address/note and auto-assigns a stable caregiver ID (cN) during execute().    * Justification: Seniors are core domain entities; risk tagging is essential for triage and filtering.
    * Justification: A human-friendly, immutable ID simplifies assignment UX and display (“c7” chip). 
    * Highlights: Designed a clean parser/command pair (AddCaregiverCommand, AddCaregiverCommandParser) and updated storage adapters/tests for the new role and risk schema.
    * Credits: Referenced the AB3 original `Person.java` and `AddCommandParser` file to maintain code consistency.
    * Issues/PRs: Fix [\#41](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/41)


* **Relationship Chips (Senior ↔ Caregiver)**
    * What it does:
        * Seniors: “Caregiver:” + one rounded chip with `caregiver name` or `Unassigned`
        * Caregivers: “Seniors:” + one rounded chip per assigned `senior name` or `Unassigned`
    * Justification: Gives instant visibility of relationships without opening details.
    * Highlights: Added chip row in PersonListCard.fxml; PersonCard#renderAssignedRow() builds chips; responsive wrapping; CSS chip styles; list refresh listener to reflect changes immediately.
    * Credits: Utilized ChatGPT with the generation of chip design for CSS code.
    * Issues/PRs: Fix [\#56](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/56) PR [\#57](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/57)


* `filter` **command**
    * What it does: Filters by tags (e.g., risk) to quickly list seniors with the corresponding tags.
    * Justification: Pairs with risk tagging for operational triage.
    * Highlights: Designed a clean parser/command pair (`FilterCommand`, `FilterCommandParser`). Documented in DG with examples.
    * Credits: Referenced the AB3 original `FindCommand.java` and `FindCommandParser` file to maintain code consistency.
    * Issues/PRs: Fix [\#69](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/69) PR [\#70](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/70) 
      

### Enhancements to existing features

* `Pin` **command Revamp**
    * Changes:
        * Previously, after the UI split into Senior/Caregiver panels, pin implicitly pinned both (pin s/1 c/1). It now pins exactly one target at a time (either a Senior or a Caregiver). 
        * The pinned person is surfaced at the top of their panel.
        * Added an improved pinned-header UI and scrolling for long content.
    * Justification: 
        * Forcing users to pin both entities at once created hidden coupling between panels. Making pin single-target is clearer, reduces accidental pinning, and better matches users’ mental model (“I want this card at hand”).
        * Quick access to “working set.” Pinning promotes the current focus item to a stable, zero-scroll location—useful in long lists where the item would otherwise drift during filtering/sorting.
        * Scrolling for long content ensures that long notes won’t take over the page
    * Highlights: Added isPinned boolean to `Senior` and `Caregiver`, updated parsers/commands/UI/tests, replaced prior indirect note-based refresh.
    * Issues/PRs: Fix [\#103](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/103) [\#104](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/104) [\#107](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/107) PR [\#110](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/110)


* `Unpin` **command Revamp**
    * Changes:
        * Previously, the unpin command accepted no parameters (unpin) and always cleared whatever was pinned.
        * Now it supports scopes for precise control: `unpin` / `unpin all` (clear both), `unpin s` (Senior only), `unpin c` (Caregiver only).
    * Justification:
        * Precision over collateral actions. Users often want to keep one target pinned while clearing the other (e.g., unpin a caregiver but keep a high-risk senior at hand). Scoped unpin prevents accidental loss of context.
        * Symmetry with pin. Since pin is now single-target, unpin should mirror that granularity for predictability and learnability.
    * Highlights: Added scoped parsing (unpin, unpin all|a, unpin s, unpin c) with helpful usage messages.
    * Issues/PRs: PR [\#110](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/110)


* **Pin UI polish**
    * Replaced the old red outline with a subtle blue pinned header for better contrast/readability.
    * Added a pin icon at the top-right of the pinned card to make the state obvious at a glance.
    * Issues/PRs:


* **Update risk tag UI from `HR/MR/LR` to `High/Medium/Low Risk`**
    * Changes: Replaced terse chips `HR`, `MR`, `LR` with fully spelled labels `High Risk`, `Medium Risk`, `Low Risk` throughout the UI.
    * Justification: Removes jargon—new users immediately understand the meaning without a legend.
    * Issues/PRs: Fix [\#60](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/60) PR [\#62](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/62)


### Bug Fixes & Quality Improvements

* **Fix assign → pinned caregiver refresh**
    * Before: After running assign s/i c/j, the assigned chips on Senior/Caregiver cards did not update immediately. The UI only refreshed after you clicked the affected card.
    * Now: I added a listener-based refresh so the UI reacts automatically:
    * A ListChangeListener/observer hooks into assignment changes and triggers a lightweight repaint. 
    * Both the main lists and the pinned header are refreshed on the JavaFX thread, so chips redraw instantly without manual clicks.
    * Credits: Utilized Chatgpt to solve the issue.
    * Issues/PRs: Fix [\#113](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/113) PR [\#114](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/114)


* **Minimum width & layout robustness**
    * Prevents cramped UI and chip wrapping glitches at narrow widths.
    * Issues/PRs: Fix [\#111](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/111) PR [\#112](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/112)


* `edit` **command: clearer unsuccessful messages**
    * Before: If you edited a Senior’s name to John and there was already a Senior named John, the app sometimes showed “Caregiver already exists.” This happened because the duplicate-name check short-circuited on the wrong list and used a caregiver-specific error string.
    * We enforce global uniqueness across roles and return a single, clear message:
      `A person with the same name already exists.`
    * Issues/PRs: Fix [\#164](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/164) PR [\#165](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/165)

### Test Cases
* Added test files and test cases for Person, AddCommand, AddCommandParser, FilterCommand, FilterCommandParser, AddressBookParser, PersonHasAnyTagPredicate, JsonAdaptedPerson with SeniorBuilder and CaregiverBuilder.

### Code contributed
* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Gracesong146&tabRepo=AY2526S1-CS2103-F13-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

### Project Management
* **Project management**:
    * Drafted and posted releases `v1.4`, `v1.5`, `v1.5.1` (3 releases) on GitHub

### Documentation
* **User Guide:**
    * Added documentation for the features `filter` (Fix) [\#72](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/72) PR [\#73](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/73)
    * Updated images to match the new layout of the UI for `pin`, `unpin`, `find`, `help` (PR) [\#169](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/169) [\#183](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/183)
    * Edited the input formats for `unpin` feature (PR) [\#184](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/184)
    * Edited `pin` and `unpin` commands under command summary table (PR) [\#186](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/186)


* **Developer Guide:**
    * Class diagram AddCommand change to AddSeniorCommand
    * Sequence diagram delete command change formatting
    * Textual updates that may no longer be relevant i.e. updating `AddCommand` to `AddSeniorCommand` or `AddCaregiverCommand` within section
    * Edited use cases and added MSS for `assign`, `unassign`, `pin`, `unpin
    * Edited `ArchitectureSequenceDiagram.puml` and `ModelClassDiagram.puml`
    * Issues/PRs: Fix [\#91](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/91) PR [\#117](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/117) [\#184](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/184)


* **Readme.md:**
    * Update the image and descriptions consistently (Fix) [\#25](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/25) [\#108](https://github.com/AY2526S1-CS2103-F13-4/tp/issues/108) PR [\#32](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/32) [\#109](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/109)

### **Community**
* PRs reviewed (with non-trivial review comments):
[\#75](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/75),
[\#78](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/78),
[\#81](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/81)

* Contributed to forum discussions (examples:
[1](https://github.com/nus-cs2103-AY2526S1/forum/issues/72),
[2](https://github.com/nus-cs2103-AY2526S1/forum/issues/79),
[3](https://github.com/nus-cs2103-AY2526S1/forum/issues/96),
[4](https://github.com/nus-cs2103-AY2526S1/forum/issues/129),
[5](https://github.com/nus-cs2103-AY2526S1/forum/issues/134)
[6](https://github.com/nus-cs2103-AY2526S1/forum/issues/185))

* Reported bugs and suggestions for other teams in the class
(examples:
[1](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/215),
[2](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/216),
[3](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/217),
[4](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/228),
[5](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/232),
[6](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/237),
[7](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/290),
[8](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/292),
[9](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/293),
[10](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/294),
[11](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/295),
[12](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/296),
[13](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/297),
[14](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/298),
[15](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/219),
[16](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/220),
[17](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/281),
[18](https://github.com/AY2526S1-CS2103T-F12-3/tp/issues/285))

**Tools**:
* Integrated a Github plugin (Codex) to personal repo in on 28 October 2025 to solve PR [\#114](https://github.com/AY2526S1-CS2103-F13-4/tp/pull/114)
