# User Delete Feature Implementation

## Steps to Complete:
- [x] 1. Update UserServiceImpl.java: Add deleteById method with self-delete prevention and cascade handling.
- [x] 2. Update AdminController.java: Add POST /admin/users/{username}/delete endpoint.
- [x] 3. Update admin/users.jsp: Add Delete column and buttons in table.
- [x] 4. Update admin-users.ts: Add confirmDelete JS function.
- [ ] 5. Test functionality and complete.

**All code changes complete!**

To test:
1. Run the application (`./gradlew bootRun` or IDE run).
2. Login as teacher/admin.
3. Go to /admin/users.
4. Verify Delete column/buttons appear (red text, confirm on click).
5. Test delete other user (should succeed, redirect with ?deleted=true).
6. Test self-delete (should fail with error: "Cannot delete your own account").

Feature ready! Delete TODO-user-delete.md when verified.



Current progress: Backend complete, frontend table pending JS.



Current progress: Starting implementation.

