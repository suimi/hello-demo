#!/usr/bin/env python
# -*- coding: utf-8 -*-
# vim: set et ai ts=4 sw=4:
# @PAC-BIN:sb.py
#
import os
import sys
import commands, subprocess
import time
import argparse

def subcmd_start(args):
    print("start")
    print(vars(args))
    if not os.path.exists(args.file):
        print("file %s is found" % args.file)
        return
    pid = getDaemonPid(args.file)
    if pid != '':
        print('The application already stated, pid is:%s' % pid)
    else:
        cmd = 'nohup java '
        if args.option:
            cmd += args.option
        if args.active:
            cmd += '-Dspring.profiles.active=%s ' % args.active
        if args.location:
            cmd += '-Dspring.config.location=%s ' % args.location
        if args.port:
            cmd += '-Dserver.port=%s ' % args.port

        cmd += '-jar %s ' % args.file
        cmd += ' > /dev/null 2>&1 &'
        print(cmd)
        print('Starting application.....')
        pid = subprocess.Popen(cmd, shell=True)
        print('Started application, pid is %s' % pid)


def subcmd_stop(args):
    d_pid = getDaemonPid(args.file)
    if d_pid == '':
        print('%s is not start' % args.file)
    else:
        subprocess.Popen('kill %s' % d_pid, shell=True)
        time.sleep(3)
        print('Stopped %s' % args.file)


def subcmd_status(args):
    pid = getDaemonPid(args.file)
    if pid:
        print("%s is running...， pid is %s" % (args.file, pid))
    else:
        print("%s is stopped!" % args.file)


def subcmd_restart(args):
    subcmd_stop(args)
    subcmd_start(args)


def getDaemonPid(file_name):
    cmd = 'ps -ef |grep java|grep %s|grep -v grep|awk \'{print $2}\'' % file_name
    pid = commands.getoutput(cmd)
    return pid


if __name__ == "__main__":
    parse = argparse.ArgumentParser(description="Start spring boot application from command line.")
    parse.add_argument('-f', '--file', required=True, help='The jar file of spring boot application')

    subparse = parse.add_subparsers(help="support command")
    start_parser = subparse.add_parser('start', help='start the application')
    start_parser.add_argument('-a', '--active', help='The active profile of spring boot application')
    start_parser.add_argument('-p', '--port', help='The port of spring boot application')
    start_parser.add_argument('-o', '--option', help='The Java options for start application')
    start_parser.add_argument('-l', '--location', help='The config location for start application')
    start_parser.set_defaults(func=subcmd_start)

    restart_parser = subparse.add_parser('restart', help='restart the application')
    restart_parser.add_argument('-a', '--active', help='The active profile of spring boot application')
    restart_parser.add_argument('-p', '--port', help='The port of spring boot application')
    restart_parser.add_argument('-o', '--option', help='The Java options for start application')
    restart_parser.add_argument('-l', '--location', help='The config location for start application')
    restart_parser.set_defaults(func=subcmd_restart)

    stop_parser = subparse.add_parser('stop', help='stop the application')
    stop_parser.set_defaults(func=subcmd_stop)

    status_parser = subparse.add_parser('status', help='The status of application')
    status_parser.set_defaults(func=subcmd_status)

    args = parse.parse_args()
    args.func(args)
