#!/usr/bin/env python

from __future__ import print_function
import os
import subprocess
import platform
import shutil

def call(cmd):
    print("{}".format(cmd))
    subprocess.call(cmd.split())

subdirs = ["p1", "p2"]

java_fname = "Attacker.java"
report_fname = "Report.pdf"
DIR = os.path.dirname(os.path.realpath(__file__))

def check_files(root):
    files = [os.path.join(root, d, f) for d in subdirs for f in [java_fname, report_fname]]
    # files = [
    #         "p1/Attacker.java",
    #         "p1/Report.pdf",
    #         "p2/Attacker.java",
    #         "p2/Report.pdf"
    #         ]
    for f in files:
        fpath = os.path.join(root, f)
        if not os.path.exists(fpath):
            raise Exception("{} not found".format(fpath))
        else:
            print(fpath)

def copy_src(src_dir, tgt_dir):
    # if not os.path.exists(tgt_dir):
    #     print("creating dir: {}".format(tgt_dir))
    #     os.makedirs(tgt_dir)
    for sub in subdirs:
        src_f = os.path.join(src_dir, sub, java_fname)
        if not os.path.exists(src_f):
            raise Exception("{} not exists".format(src_f))
        tgt_parent = os.path.join(tgt_dir, sub)
        if not os.path.exists(tgt_parent):
            print("creating {}".format(tgt_parent))
            os.makedirs(tgt_parent)
        tgt_f = os.path.join(tgt_parent, java_fname)
        print("{} => {}".format(src_f, tgt_f))
        shutil.copy(src_f, tgt_f)

def run_sbt():
    if platform.system() in ["Darwin", "Linux"]:
        sbt = os.path.join(DIR, "sbt")
    else:
        sbt = os.path.join(DIR, "sbt.bat")
    cmd = "{} test".format(sbt)
    call(cmd)

submit_dir = os.path.join(DIR, "SUBMIT")
src_root = os.path.join(DIR, "src/main/java")

def collect_files():
    copy_src(src_root, submit_dir)

if __name__ == "__main__":
    run_sbt()
    collect_files()
