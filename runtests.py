#!/usr/bin/env python

from __future__ import print_function

import subprocess
import zipfile

import os


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
        os.rename(src_java, src_root)
        prompt("{} => {}".format(src_java, tgt_java))


def sbttest():
    cmd = "sbt test"
    return call(cmd)


ziproot = os.path.join(DIR, os.path.pardir, "cz4024_submissions")


def zipto(zipfilename):
    zip_ref = zipfile.ZipFile(zipfilename, 'r')
    zip_ref.extractall(ziproot)
    zip_ref.close()
    zipdir_name = zipfilename[:-4]
    zipdir = os.path.join(ziproot, zipdir_name)
    return zipdir


def run_once(zipfilename):
    prompt("\n{} STARTING {} {}".format("=" * 40, zipfilename, "=" * 40))
    zipdir = zipto(zipfilename)
    copy_src(zipdir, project_src_root)
    retcode = sbttest()
    if retcode != 0:
        report(user)
    else:
        prompt("DONE: {}".format(zipfilename))


if __name__ == '__main__':
    zipfname = "./STUDENTS/CECZ4024_123.zip"
    run_once(zipfname)
