# Lecture Deletion Fix

**Diagnosis**: FK constraint violation - materials reference lecture, can't delete parent.

**Plan**:
1. Edit `LectureServiceImpl.deleteById()` - add material deletion before lecture
2. Inject `ICourseMaterialService`
3. `@Transactional` cascade delete materials

**Dependent**: None

**Followup**: Test delete lecture with materials → success, no crash.

Approve?

