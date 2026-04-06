# Admin User Management View Enhancement

## Approved Plan:
- Reuse user/profile.jsp 
- Teacher can edit fullName, email, phoneNumber
- User sees changes, can't update username/role/status
- URL: /admin/users/{id}/view (ID)
- Steps:
- [x] 1. Fix users.jsp: `${user.userId}` → `${user.username}` ✓
- [x] 2. Fix Controller viewUser(username) ✓
- [x] 3. Edit profile.jsp: adminView back link + dynamic form action `/admin/users/${targetUsername}/update` ✓
- [x] 4. Add AdminController.updateUser POST ✓
- [ ] 5. Update TODO
- [ ] 6. Test

## Next: Step 1
