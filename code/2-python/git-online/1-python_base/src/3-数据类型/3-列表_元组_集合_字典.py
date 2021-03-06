#coding=gbk

#string、list和tuple都属于sequence（序列）。

# 列表_元组_集合_字典
# List（列表）  Tuple（元组） Set（集合） Dictionary（字典）

#列表，任意对象的有序集合，  通过偏移存取，  可变长度、异构、任意联动，  属于序列可变的类型。

L1 = [] #一个空列表， 列表使用的是中括号表示。
L2 = [0,1,3,4]
L3 = ['1','2','3']
L4 = ["1",["嵌套1","嵌套2"]]


L = [L1,L2,L3,L4]

print(L)

#元组。任意对象的有序集合，  通过偏移存取，  固定长度、异构、任意联动，  属于序列不可变的类型。

T1 = () #空的元组，元组使用小括号表示。
T2 = ('1','2','3',4)
T3 = 1,2,3,4
T4 = (1,(2,3))

T = [T1,T2,T3,T4]

print(T)

#集合 任意对象的无需集合，  

#可以使用大括号({})或者 set()函数创建集合
S1 = set() #创建一个空集合必须用 set() 而不是 { }，因为 { } 是用来创建一个空字典。
S2 = set('TomRose')
S3 = {'Tom', 'Jim', 'Mary', 'Tom', 'Jack', 'Rose'}

S = [S1,S2,S3]

print(S)

#字典。任意对象的无序集合，  通过键而不是偏移存取，  可变长度、异构、任意联动，  属于可变映射的类型。

D1 = {} #空的字典，字典使用大括号
D2 = {'age':18,'name':"wang"}
D3 = {'people':{'person1':'wang','person2':'yan'}}
D4 = {}  
D4['key'] = 'key'

#通过Key进行取值
V1 = D3['people']

D = [D1,D2,D3,D4]
V = [V1]

print(D)
print(V)
