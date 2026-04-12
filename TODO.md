# Fix 405 Error on Language Switch After Failed Admin Username Update

## Plan Steps:
- [x] 1. Create TODO.md (done)
- [x] 2. Edit AdminController.java: Change error handling from forward to redirect:/admin/users/{username}/view?error=true
- [x] 3. Edit profile.jsp: Add error message display for ?error param
- [ ] 4. Restart server with ./gradlew bootRun
- [ ] 5. Test: Login teacher -> /admin/users -> view user -> update to invalid newUsername (e.g. duplicate/exists) -> verify redirect with error -> switch lang -> no 405
- [ ] 6. Update TODO.md with completion
- [ ] 7. attempt_completion

Current progress: Code edits complete.

