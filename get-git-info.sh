#!/bin/bash

echo "revision=`git rev-parse HEAD`"
echo "branch=`git branch | awk '/^\\* / { print $2 }'`"
echo "url=`git remote -v | awk '/fetch/ { print $2 }'`"