# Branch Prefix

- **Feature**: developing new features. `feature/log-system`.
- **Bugfix**: fixing bugs in the code. `bugfix/service-validation`.
- **Hotfix**: fixing critical bugs in the production environment. `hotfix/critical-login-issue`.
- **Release**: preparing new releases with small adjustments. `release/1.0.2`.
- **Documentation**: write, update, or fix documentation. `docs/javadoc/services`.

# Commit Title
Include the branch pre-fix for better clarity of what is being changed `feat: add validator`.
Keep it short and descriptive. Less is more. <br/>
Breaking changes should be marked with `!`, e.g. `feat!: login requires new token`.

# Commit Description
Keep is short in multiple lines for easier read. Be descriptive and direct of what was changed.
If it was a braking changing, use `!` to  show where the breaking changed affected the previous version.<br/>
```text
Updated User Service logic new
!Token has been introduced for better security
```