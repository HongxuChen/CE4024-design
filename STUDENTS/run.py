#!/usr/bin/env python

from __future__ import print_function

import os
import shutil
import subprocess
import sys


def prompt(msg):
    print("> {}".format(msg))


def error(msg):
    print("ERROR: {}".format(msg), file=sys.stderr)


subdirs = ["p1", "p2"]

java_fname = "Attacker.java"
report_fname = "Report.pdf"
DIR = os.path.dirname(os.path.realpath(__file__))
DIR = os.path.relpath(DIR, os.path.curdir)
submit_dir = os.path.join(DIR, "CECZ4024")
src_root = os.path.join(DIR, "src")
pdf_root = os.path.join(DIR, "pdf")


def _is_pdfs_ready(pdf_root):
    src_pdfs = [os.path.join(pdf_root, d, report_fname) for d in subdirs]
    for pdf in src_pdfs:
        if not os.path.isfile(pdf):
            error("{} not exists".format(pdf))
            return False
    return True


def _final_check(root):
    files = [os.path.join(root, d, f) for d in subdirs for f in [java_fname, report_fname]]
    for fpath in files:
        if not os.path.exists(fpath):
            error("{} not found".format(fpath))
            exit(1)
        else:
            prompt("* {}".format(fpath))


def _copy_src(src_dir, tgt_dir, fname):
    for sub in subdirs:
        src_f = os.path.join(src_dir, sub, fname)
        if not os.path.exists(src_f):
            error("{} not exists".format(src_f))
            exit(1)
        tgt_parent = os.path.join(tgt_dir, sub)
        if not os.path.exists(tgt_parent):
            prompt("creating {}".format(tgt_parent))
            os.makedirs(tgt_parent)
        tgt_f = os.path.join(tgt_parent, fname)
        prompt("{} => {}".format(src_f, tgt_f))
        shutil.copy(src_f, tgt_f)


def _rename_submit(root):
    prompt("ensure you have collected all files before submission")
    _final_check(root)
    prompt("""append directory name {} with your matriculation No.
    WARNING: will remove old directory if exists.""".format(root))
    number = raw_input("\nInput your matriculation No.: ")
    print()
    final_root = root + "_" + number
    if os.path.isdir(final_root):
        prompt("removing all files in old submission folder")
        shutil.rmtree(final_root, True)
    prompt("{} => {}".format(root, final_root))
    os.rename(root, final_root)
    zipfname = final_root + ".zip"
    if os.path.isfile(zipfname):
        prompt("removing OLD {}".format(zipfname))
        os.remove(zipfname)
    prompt("zip {} to {}".format(final_root, zipfname))
    shutil.make_archive(final_root, 'zip', final_root)


def collect_files():
    prompt("copy java files")
    _copy_src(src_root, submit_dir, java_fname)
    if not _is_pdfs_ready(pdf_root):
        error("make sure your pdfs are ready")
        exit(1)
    prompt("copy report files")
    _copy_src(pdf_root, submit_dir, report_fname)
    _rename_submit(submit_dir)
    prompt("DONE!")


if __name__ == "__main__":
    collect_files()
