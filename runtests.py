#!/usr/bin/env python3

import os
import shutil
import subprocess
import sys

# DON'T record exact time
# when timeout, double-check


def call(cmd, timeout=None):
    if timeout is not None:
        print("{} timeout={}s".format(cmd, timeout))
    else:
        print("{}".format(cmd))
    return subprocess.call(cmd.split(), timeout)


DIR_ABS = os.path.dirname(os.path.realpath(__file__))
DIR = os.path.relpath(DIR_ABS, os.path.curdir)

project_src_root = "src/main/java"
P_NUMS = [str(i) for i in [1,2]]


def prompt(msg):
    print("> {}".format(msg))


def warn(msg):
    print("* {}".format(msg), file=sys.stderr)


def report(msg):
    print("FAILED: {}".format(msg), file=sys.stderr)


def get_filename(number, ext, prefix="Attacker"):
    return prefix + number + "." + ext


def copy_src(src_root, tgt_root):
    for i in P_NUMS:
        f_name = get_filename(i, "java")
        src_java = os.path.join(src_root, f_name)
        tgt_java = os.path.join(tgt_root, f_name)
        prompt("{} => {}".format(src_java, tgt_java))
        shutil.copyfile(src_java, tgt_java)


def sbt_test(submission):
    cmd = "sbt test"
    try:
        call(cmd, timeout=120)
    except subprocess.TimeoutExpired as e:
        report(submission)


def git_check():
    cmd = "git checkout {}".format(project_src_root)
    call(cmd, timeout=2)


def run_once(submission_dir):
    copy_src(submission_dir, project_src_root)
    sbt_test(submission_dir)
    input("Press Enter to continue...")
    git_check()


def run_many(root):
    for submission in os.listdir(root):
        file_path = os.path.join(root, submission)
        if not os.path.isdir(file_path):
            warn("{} not dir, ignore".format(file_path))
        else:
            run_once(file_path)
    prompt("DONE!")

if __name__ == '__main__':
    submissions = os.path.realpath(os.path.join(DIR, os.path.pardir, "markable"))
    run_many(submissions)
