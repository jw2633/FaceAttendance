# -*- coding: utf-8 -*-
import sys
import MySQLdb
import numpy as np
import pandas as pd

professor = sys.argv[1]
className = sys.argv[2]

db = MySQLdb.connect("db.sice.indiana.edu", "i494f18_team72", "my+sql=i494f18_team72", "i494f18_team72")
cursor = db.cursor()

if len(sys.argv) == 4:
	studentName = sys.argv[3]

	sql = 'SELECT * FROM %s_s_att WHERE class="%s" and name="%s"' % (professor, className, studentName)
	cursor.execute(sql)
	sql_results = cursor.fetchall()

	results_a = np.array(sql_results)
	print(results_a)
elif len(sys.argv) == 6:
	date0 = sys.argv[3]+'-'+sys.argv[4]+'-'+sys.argv[5]

	sql = 'SELECT * FROM %s_s_att WHERE class="%s"' % (professor, className)
	cursor.execute(sql)
	sql_results = cursor.fetchall()

	results_a = np.array(sql_results)
	results_p = pd.DataFrame(data=results_a,columns=['id','name','class','time'])
	results_p['date'] = [item[:10] for item in results_p.time]
	results_p = results_p.loc[results_p.date==date0,:].iloc[:,:-1]
	results_p.to_csv("attendance.csv", index = 0)
	print(results_p.values)

db.close()



