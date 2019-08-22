import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import tensorflow as tf
first_blood = tf.constant('double kill')
sess = tf.Session()
print(str(sess.run(first_blood)))