#!/usr/bin/python

from os import listdir, sys
from os.path import isfile, join
import random

mypath = "."

# Get files in the current directory
files = [ f for f in listdir(mypath) if isfile(join(mypath,f))]

# Randomize them!
random.shuffle(files)

# Output to the screen
for file in files:
	if file != sys.argv[0]:
		idx = file.rindex(".")
		file = file[0:idx]
		print file
