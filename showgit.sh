#!/bin/bash

TortoiseGitProc.exe  /command:repostatus &
sleep 0.250
TortoiseGitProc.exe  /command:sync &
