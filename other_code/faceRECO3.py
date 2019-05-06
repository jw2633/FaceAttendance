# -*- coding: utf-8 -*-
import numpy as np
import heapq
import MySQLdb
import sys
import time

with open('android_connect/encoding.txt', 'r') as f:
    encoding_oo = f.readlines()[0][:-1]

encoding_o = encoding_oo.split(",")
encoding_o = [float(item) for item in encoding_o]

db = MySQLdb.connect("db.sice.indiana.edu", "i494f18_team72", "my+sql=i494f18_team72", "i494f18_team72")
cursor = db.cursor()

professor = sys.argv[1]
className = sys.argv[2]
if len(sys.argv)==3:
    sql = 'SELECT id, name, encoding FROM %s_s_info WHERE class="%s"' % (professor, className)
    cursor.execute(sql)
    sql_results = cursor.fetchall()
    ids = [int(item[0]) for item in sql_results]
    names = [item[1] for item in sql_results]
    encodings = [item[2].split(",") for item in sql_results]

    dist = []
    for i in range(len(encodings)):
        encodings[i] = [float(item) for item in encodings[i]]
        dist_temp = np.sqrt(np.sum(np.square(np.subtract(encodings[i], encoding_o))))
        dist.append(dist_temp)
        
    if min(dist)>1:
        print("NOT IN DATABASE")
    else:
        result_index = map(dist.index, heapq.nsmallest(1, dist))[0]
        result_id = ids[result_index]
        result_name = names[result_index]
        print("Welcome %s!" % result_name)
        current_time = time.strftime('%Y-%m-%d_%H:%M:%S',time.localtime(time.time()))
        sql2 = 'INSERT INTO %s_s_att (id,name,class,time) VALUES (%i,"%s","%s","%s")' % (professor,result_id,result_name,className,current_time)
        cursor.execute(sql2)
        db.commit()
elif len(sys.argv)==5:
    studentID = int(sys.argv[3])
    studentName = sys.argv[4]
    sql3 = 'SELECT * FROM %s_s_info WHERE id=%i' % (professor, studentID)
    cursor.execute(sql3)
    sql_results = cursor.fetchall()
    if(len(sql_results)>0):
        sql4 = 'UPDATE %s_s_info SET encoding="%s" WHERE id=%i' % (professor, encoding_oo, studentID)
        cursor.execute(sql4)
        db.commit()
        print("Updating %s's photo completed" % studentName)
    elif(len(sql_results)==0):
        sql4 = 'INSERT INTO %s_s_info (id,name,class,encoding) VALUES (%i,"%s","%s","%s")' % (professor,studentID,studentName,className,encoding_oo)
        cursor.execute(sql4)
        db.commit()
        print("Uploading %s's information completed" % studentName)

db.close()


