# -*- coding: utf-8 -*-
import sys

with open('android_connect/encoding.txt', 'a') as f:
    f.write(sys.argv[1])
    f.write(',')


