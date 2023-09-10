# Contributing to the project

- [Contributing to the project](#contributing-to-the-project)
  - [Restricted branches](#restricted-branches)
  - [Workflow](#workflow)
    - [Day Start](#day-start)
    - [Start New Feature/Bugfix](#start-new-featurebugfix)
    - [Update Feature Branch](#update-feature-branch)
      - [Merge](#merge)
    - [Commit and Push](#commit-and-push)

## Restricted branches

- `develop` - all the implemented features which are done and deployed
- `master` - stable version deployed

## Workflow

### Day Start

Use `git pull` in order to retrieve the most recent commits from GitHub.

### Start New Feature/Bugfix

A new branch to implement a feature/task from a JIRA story must have the name/title of the form `FDP-#-<story>-<title>` where '#' is the JIRA story/task number, for example `FDP-9-member-registration`.

In order to minimize merge conflicts later always open a new feature branch from the most recent state of the `develop` branch on GitHub.

```bash
git checkout develop
git pull
git checkout -b <branch_name>
```

### Update Feature Branch

While you're working on your own feature/bugfix other developers make changes on `develop` and it's required to update your branch to keep consistency of the codebase. In this project we will be using the `git merge` approach

#### Merge

```bash
git checkout develop
git pull
git checkout <branch_name>
git merge develop
```

### Commit and Push

Always merge `develop` before pushing the branch to origin.

Once merged you can simply checkout the branch and push to origin after resolving any merge conflicts.

```bash
git checkout <branch_name>
git push origin <branch_name>
```