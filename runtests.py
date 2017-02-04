#!/usr/bin/env python

from __future__ import print_function

import os
import shutil
import subprocess
import zipfile


def call(cmd):
    print("{}".format(cmd))
    return subprocess.call(cmd.split())


DIR_ABS = os.path.dirname(os.path.realpath(__file__))
DIR = os.path.relpath(DIR_ABS, os.path.curdir)

subdirs = ["p1", "p2"]
java_fname = "Attacker.java"
project_src_root = "src/main/java"


def prompt(msg):
    print("> {}".format(msg))


def warn(msg):
    print("* {}".format(msg))


def report(msg):
    print("FAILED: {}".format(msg))


def copy_src(src_root, tgt_root):
    for d in subdirs:
        src_java = os.path.join(src_root, d, java_fname)
        tgt_java = os.path.join(tgt_root, d, java_fname)
        if os.path.exists(tgt_java):
            warn("{} exists".format(tgt_java))
        prompt("{} => {}".format(src_java, tgt_java))
        shutil.copyfile(src_java, tgt_java)


def sbttest():
    cmd = "sbt test"
    return call(cmd)


ziproot = os.path.join(DIR, os.path.pardir, "cecz4024_submissions")
if not os.path.isdir(ziproot):
    os.makedirs(ziproot)


def zipto(zipfpath, zipfilename):
    zip_ref = zipfile.ZipFile(zipfpath, 'r')
    zipdir_name = zipfilename[:-4]
    zipdir = os.path.join(ziproot, zipdir_name)
    zip_ref.extractall(zipdir)
    zip_ref.close()
    return zipdir

def run_once(zipfpath):
    prompt("{} STARTING {} {}".format("=" * 40, zipfpath, "=" * 40))
    zipfilename = os.path.basename(zipfpath)
    zipdir = zipto(zipfpath, zipfilename)
    copy_src(zipdir, project_src_root)
    retcode = sbttest()
    if retcode != 0:
        report(zipfilename)
    else:
        prompt("DONE: {}".format(zipfilename))

def run_many(root):
    for f in os.listdir(root):
        if f.endswith(".zip"):
            fpath = os.path.join(root, f)
            prompt("=== {}".format(fpath))
            run_once(fpath)
    prompt("DONE!")

zipfile_root = os.path.join(DIR, "STUDENTS")

if __name__ == '__main__':
    bak_root = os.path.join(ziproot, "bak")
    run_many(zipfile_root)
    cmd = "git checkout {}".format(project_src_root)
    call(cmd)
