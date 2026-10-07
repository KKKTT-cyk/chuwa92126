# Chuwa Training

Assignment submission repo. Put coding homework under `Coding/` and short-answer questions under `ShortQuestions/`.

```
.
├── Coding/           # coding assignments (e.g. Coding/hw1/...)
└── ShortQuestions/   # written answers (e.g. ShortQuestions/hw1.md)
```

## How to submit your assignments using this repo

### Step 1: Fork this repo

Students **must fork** this repository to their own GitHub account first.

Click **Fork** on the top-right corner of this repo.

### Step 2: Clone *your forked repo*

```bash
cd your_work_dir
git clone https://github.com/<your_github_username>/chuwa92126.git
cd chuwa92126
```

(Optional but recommended) Add the original repo as upstream:

```bash
git remote add upstream https://github.com/KKKTT-cyk/chuwa92126.git
```

### Step 3: Create your own master branch

Create a personal master branch in your fork:

```bash
git checkout -b firstName_lastName/master
git push origin firstName_lastName/master
```

### Step 4: Create a homework feature branch

For each homework, create a new branch from your own master branch:

```bash
git checkout firstName_lastName/master
git checkout -b firstName_lastName/hw1
```

> **Branch names matter.** The submissions table below is generated automatically from the branch name,
> so it must be `firstName_lastName/hwN` (e.g. `tingyu_chang/hw3`). Other names may not be counted.

Work on your assignment, then commit and push:

```bash
git add .
git commit -m "Finish HW1"
git push origin firstName_lastName/hw1
```

### Step 5: Raise a Pull Request (PR)

1. Go to your fork on GitHub.
2. Click **Compare & pull request** (or **Pull requests → New pull request**).
3. Set the branches:
   - **base repository**: `KKKTT-cyk/chuwa92126`, **base**: `main`
   - **head repository**: `<your_github_username>/chuwa92126`, **compare**: `firstName_lastName/hw1`
4. Title the PR `firstName_lastName hw1` and click **Create pull request**.

## HW Submissions

Updated automatically by [`.github/workflows/submissions.yml`](.github/workflows/submissions.yml) whenever a PR is opened, closed or edited. Do not edit this table by hand.

<!-- SUBMISSIONS:START -->
| GitHub | Student Name | HW | PR Status | Submitted At | PR URL |
|--------|--------------|----|-----------|--------------|--------|
| allenk416 | Yan Zhang | hw1 | open | 2026-09-24 11:01:47 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/8) |
| allenk416 | Yan Zhang | hw2 | open | 2026-09-29 00:29:23 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/45) |
| allenk416 | Yan Zhang | hw3 | open | 2026-09-29 00:29:34 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/46) |
| allenk416 | Yan Zhang | hw4 | open | 2026-10-01 23:41:21 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/57) |
| allenk416 | Yan Zhang | hw5 | open | 2026-10-06 00:29:11 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/76) |
| Ericsayhelloworld | Sicheng Xue | hw1 | open | 2026-09-24 02:16:10 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/5) |
| Ericsayhelloworld | Sicheng Xue | hw2 | open | 2026-09-25 19:05:38 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/16) |
| Ericsayhelloworld | Sicheng Xue | hw3 | open | 2026-09-28 18:19:35 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/26) |
| Ericsayhelloworld | Sicheng Xue | hw4 | open | 2026-09-30 01:40:39 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/50) |
| Ericsayhelloworld | Sicheng Xue | hw5 | open | 2026-10-04 23:46:34 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/63) |
| esteng24 | Chenye Wu | hw1 | open | 2026-09-24 23:13:26 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/11) |
| esteng24 | Chenye Wu | hw2 | open | 2026-09-28 21:16:59 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/32) |
| esteng24 | Chenye Wu | hw3 | open | 2026-09-28 21:06:06 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/29) |
| esteng24 | Chenye Wu | hw4 | open | 2026-10-01 23:09:02 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/56) |
| esteng24 | Chenye Wu | hw5 | open | 2026-10-05 22:48:01 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/73) |
| esteng24 | Chenye Wu | hw6 | open | 2026-10-05 23:04:05 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/74) |
| Irenezhangtt | Yutong Zhang | hw1 | open | 2026-09-25 21:13:59 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/19) |
| Irenezhangtt | Yutong Zhang | hw2 | open | 2026-09-25 21:12:59 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/18) |
| Irenezhangtt | Yutong Zhang | hw3 | open | 2026-09-26 21:10:03 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/21) |
| Irenezhangtt | Yutong Zhang | hw4 | open | 2026-10-02 00:30:41 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/60) |
| Irenezhangtt | Yutong Zhang | hw5 | open | 2026-10-06 01:11:51 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/77) |
| IShinji | Hongquan Zou | hw1 | open | 2026-09-24 23:45:07 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/12) |
| IShinji | Hongquan Zou | hw2 | open | 2026-09-28 23:19:06 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/39) |
| IShinji | Hongquan Zou | hw3 | open | 2026-09-29 00:07:00 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/41) |
| IShinji | Hongquan Zou | hw4 | open | 2026-10-02 00:29:02 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/59) |
| IShinji | Hongquan Zou | hw5 | open | 2026-10-05 21:55:19 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/68) |
| IShinji | Hongquan Zou | hw6 | open | 2026-10-05 21:58:39 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/69) |
| iychi | Iyu Lin | hw1 | open | 2026-09-28 22:06:31 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/37) |
| iychi | Iyu Lin | hw2 | open | 2026-09-28 22:27:42 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/38) |
| iychi | Iyu Lin | hw3 | open | 2026-09-29 00:30:56 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/47) |
| iychi | Iyu Lin | hw4 | open | 2026-10-02 00:20:31 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/58) |
| iychi | Iyu Lin | hw5 | open | 2026-10-05 22:46:27 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/72) |
| jane0x5a | Jie Zhang | hw1 | open | 2026-09-24 00:44:49 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/3) |
| jane0x5a | Jie Zhang | hw2 | open | 2026-09-28 18:40:49 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/27) |
| jane0x5a | Jie Zhang | hw3 | open | 2026-09-28 18:41:50 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/28) |
| jane0x5a | Jie Zhang | hw4 | open | 2026-10-01 01:03:36 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/52) |
| jane0x5a | Jie Zhang | hw5 | open | 2026-10-05 18:47:41 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/66) |
| lantshoe | Jie Yu | hw1 | open | 2026-09-24 02:59:21 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/6) |
| lantshoe | Jie Yu | hw2 | open | 2026-09-28 21:14:53 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/30) |
| lantshoe | Jie Yu | hw3 | open | 2026-09-28 21:15:50 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/31) |
| lantshoe | Jie Yu | hw4 | open | 2026-10-05 22:40:39 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/71) |
| MarlonZhang | Xudong Zhang | hw2 | open | 2026-09-29 00:15:42 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/42) |
| MarlonZhang | Xudong Zhang | hw4 | open | 2026-10-07 03:45:14 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/79) |
| mimidamimi | Meixin Wu | hw1 | open | 2026-09-23 18:56:02 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/2) |
| mimidamimi | Meixin Wu | hw2 | open | 2026-09-27 18:59:22 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/23) |
| mimidamimi | Meixin Wu | hw3 | open | 2026-09-29 16:32:44 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/49) |
| mimidamimi | Meixin Wu | hw4 | open | 2026-10-03 20:09:20 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/62) |
| mimidamimi | Meixin Wu | hw5 | open | 2026-10-06 22:35:44 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/78) |
| siwenwu24 | Siwen Wu | hw1 | open | 2026-09-29 00:41:39 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/48) |
| siwenwu24 | Siwen Wu | hw2 | open | 2026-09-29 00:26:25 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/44) |
| siwenwu24 | Siwen Wu | hw3 | open | 2026-09-29 00:24:32 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/43) |
| siwenwu24 | Siwen Wu | hw4 | open | 2026-10-05 23:36:06 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/75) |
| siwenwu24 | Siwen Wu | hw5 | open | 2026-10-07 04:53:24 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/80) |
| tiffiong | Tiffany Iong | hw1 | open | 2026-09-23 14:04:37 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/1) |
| tiffiong | Tiffany Iong | hw2 | open | 2026-09-28 16:13:54 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/25) |
| tiffiong | Tiffany Iong | hw3 | open | 2026-10-01 21:41:11 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/55) |
| tiffiong | Tiffany Iong | hw4 | open | 2026-10-05 18:35:16 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/65) |
| VincentSWH | Weihan Song | hw1 | open | 2026-09-24 23:58:18 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/13) |
| VincentSWH | Weihan Song | hw2 | open | 2026-09-28 21:40:53 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/34) |
| VincentSWH | Weihan Song | hw3 | open | 2026-09-28 23:49:25 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/40) |
| VincentSWH | Weihan Song | hw4 | open | 2026-10-02 23:41:41 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/61) |
| wdtt057 | Dengtai Wang | hw1 | open | 2026-09-24 03:41:29 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/7) |
| wdtt057 | Dengtai Wang | hw2 | open | 2026-09-28 06:12:27 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/24) |
| wdtt057 | Dengtai Wang | hw3 | open | 2026-09-28 22:06:16 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/36) |
| wdtt057 | Dengtai Wang | hw4 | open | 2026-10-01 07:38:18 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/53) |
| wdtt057 | Dengtai Wang | hw5 | open | 2026-10-05 20:20:29 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/67) |
| YiboDing1998 | Yibo Ding | hw1 | open | 2026-09-24 01:52:27 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/4) |
| YiboDing1998 | Yibo Ding | hw2 | open | 2026-09-27 02:34:44 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/22) |
| YiboDing1998 | Yibo Ding | hw3 | open | 2026-09-28 21:54:41 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/35) |
| YiboDing1998 | Yibo Ding | hw4 | open | 2026-10-01 20:52:51 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/54) |
| YiboDing1998 | Yibo Ding | hw5 | open | 2026-10-05 22:27:15 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/70) |
| zhengyicoding | Zhengyi Xu | hw1 | open | 2026-09-24 17:06:57 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/9) |
| zhengyicoding | Zhengyi Xu | hw2 | open | 2026-09-26 03:39:56 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/20) |
| zhengyicoding | Zhengyi Xu | hw3 | open | 2026-09-28 21:30:53 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/33) |
| zhengyicoding | Zhengyi Xu | hw4 | open | 2026-09-30 19:17:12 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/51) |
| zhengyicoding | Zhengyi Xu | hw5 | open | 2026-10-05 00:10:08 | [link](https://github.com/KKKTT-cyk/chuwa92126/pull/64) |
<!-- SUBMISSIONS:END -->
