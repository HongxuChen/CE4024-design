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

project_src_root = "src/main/java"
P_NUMS = [str(i) for i in [1,2]]


def prompt(msg):
    print("> {}".format(msg))


def warn(msg):
    print("* {}".format(msg))


def report(msg):
    print("FAILED: {}".format(msg))

def get_fname(number, ext, prefix="Attacker"):
    return prefix + number + "." + ext

def copy_src(src_root, tgt_root):
    for i in P_NUMS:
        f_name = get_fname(i, "java")
        src_java = os.path.join(src_root, f_name)
        tgt_java = os.path.join(tgt_root, f_name)
        if os.path.exists(tgt_java):
            warn("{} exists".format(tgt_java))
        prompt("{} => {}".format(src_java, tgt_java))
        shutil.copyfile(src_java, tgt_java)

def sbttest():
    cmd = "sbt test"
    return call(cmd)


ziproot = os.path.join(DIR, os.path.pardir, "cecz4024_submissions")
print("ziproot={}".format(ziproot))
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
    for f in os.listdir(ziproot):
        print(f)
        if f.endswith(".zip"):
            fpath = os.path.join(ziproot, f)
            prompt("=== {}".format(fpath))
            run_once(fpath)
    prompt("DONE!")

zipfile_root = os.path.join(DIR, "STUDENTS")

if __name__ == '__main__':
    run_many(zipfile_root)
    cmd = "git checkout {}".format(project_src_root)
    call(cmd)
