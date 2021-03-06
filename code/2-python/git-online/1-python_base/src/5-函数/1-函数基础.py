#coding=gbk

#函数典型情况

'''
def 生成一个函数对象，并赋值它一个名字
  Python用一个新语句def写函数，在运行时，def是一个可执行语句。它生成一个函数对象并给它取一个函数名。函数名字成为对函数对象的一个引用。
return 给调用者返回一个结果对象
  一个函数被调用后，要等到函数完成了工作并将控制权返回给调用者，调用才算结束。函数用一个return语句计算一个值，并返回给调用者。
global 声明模块级被赋值的变量
  默认情况下，所有对函数命名的赋值都是局部的，只在函数运行时存在。为了在一个包含它的模块中赋值一个名字，函数需要在一个global语句中列出它。
参数通过赋值传递(对象引用)
  在Python中，函数通过赋值传递参数。调用者和函数通过引用共享对象，但没有别名。
参数、返回类型和变凉不用声明
  像在Python中的所有东西一样，在函数上没有类型的约束。事实上，不需要实现对函数声明什么，我们在参数中可以传递任何类型，返回任何种类的对象，等到。因此，一个单一的函数通常可以应用在大量的对象类型上。
'''

'''
def <名字>(arg1,arg2,...):
   <语句>
 return <值>
'''

"""可以通过给出一个 doc string (文档字符串) 文档化一个 Python 函数。
    这里就是 定义 本 函数的 doc string.
    在三重引号中的任何东西都是这个函数的 doc string, 它们用来说明函数可以做什么。 
    如果存在 doc string, 它必须是一个函数要定义的第一个内容( 也就是说, 在冒号后面的第一个内容 )。
"""

def check(seq1,seq2):
  """可以通过给出一个 doc string (文档字符串) 文档化一个 Python 函数。
      这里就是 定义 本 函数的 doc string.
      在三重引号中的任何东西都是这个函数的 doc string, 它们用来说明函数可以做什么。 
      如果存在 doc string, 它必须是一个函数要定义的第一个内容( 也就是说, 在冒号后面的第一个内容 )。
  """
  res = []
  for x in seq1:
    if x in seq2:
     res.append(x)
  return res

#缩进，同一个层次的语句，缩进在一个层级就好。
def check(seq1,seq2):
      res = []
      for x in seq1:
        if x in seq2:
         res.append(x)
      return res

#同一个函数名可以多次应用，以最后一个为准。
def check(seq1,seq2):
 return seq1 + seq2

s1 = "asdf"
s2 = "sfgh"
print(check(s1,s2))