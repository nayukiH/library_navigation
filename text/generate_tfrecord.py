from __future__ import division  
from __future__ import print_function  
from __future__ import absolute_import  
  
import os  
import io  
import pandas as pd  
import tensorflow as tf  
  
from PIL import Image  
from object_detection.utils import dataset_util  
from collections import namedtuple, OrderedDict  
  
flags = tf.app.flags  
flags.DEFINE_string('csv_input', '', 'Path to the CSV input')  
flags.DEFINE_string('output_path', '', 'Path to output TFRecord')  
FLAGS = flags.FLAGS  
# TO-DO replace this with label map  
def class_text_to_int(row_label,filename):
    if row_label == 'Borrow_machine':
        return 1  
    elif row_label == 'hall':
        return 2 
    elif row_label == 'retriArea_pc':
        return 3
    elif row_label == 'tech1':
        return 4
    elif row_label == 'tech2':
        return 5
    # elif row_label == 'inferno':
    #     return 6
    # elif row_label == 'stone_blame':
    #     return 7
    # elif row_label == 'green_jelly':
    #     return 8
    # elif row_label == 'blue_jelly':
    #     return 9
    # elif row_label == 'box':
    #     return 10
    # elif row_label == 'golden_box':
    #     return 11
    # elif row_label == 'silver_box':
    #     return 12
    # elif row_label == 'jar':
    #     return 13
    # elif row_label == 'purple_jar':
    #     return 14
    # elif row_label == 'purple_weapon':
    #     return 15
    # elif row_label == 'blue_weapon':
    #     return 16
    # elif row_label == 'blue_shoe':
    #     return 17
    # elif row_label == 'blue_barde':
    #     return 18
    # elif row_label == 'blue_ring':
    #     return 19
    # elif row_label == 'badge':
    #     return 20
    # elif row_label == 'dragon_stone':
    #     return 21
    # elif row_label == 'lawn':
    #     return 22
    # elif row_label == 'mine':
    #     return 23
    # elif row_label == 'portal':
    #     return 24
    # elif row_label == 'tower':
    #     return 25
    # elif row_label == 'hero_stone':
    #     return 26
    # elif row_label == 'oracle_stone':
    #     return 27
    # elif row_label == 'arena':
    #     return 28
    # elif row_label == 'gold_ore':
    #     return 29
    # elif row_label == 'relic':
    #     return 30
    # elif row_label == 'ancient':
    #     return 31
    # elif row_label == 'house':
    #     return 32
    else:
        print("------------------nonetype:", filename)
        None
  
def split(df, group):  
    data = namedtuple('data', ['filename', 'object'])  
    gb = df.groupby(group)  
    return [data(filename, gb.get_group(x)) for filename, x in zip(gb.groups.keys(), gb.groups)]  
  
  
def create_tf_example(group, path):  
    with tf.gfile.GFile(os.path.join(path, '{}'.format(group.filename)), 'rb') as fid:  
        encoded_jpg = fid.read()  
    encoded_jpg_io = io.BytesIO(encoded_jpg)  
    image = Image.open(encoded_jpg_io)  
    width, height = image.size  
  
    filename = group.filename.encode('utf8')  
    image_format = b'jpg'  
    xmins = []  
    xmaxs = []  
    ymins = []  
    ymaxs = []  
    classes_text = []  
    classes = []  
  
    for index, row in group.object.iterrows():  
        xmins.append(row['xmin'] / width)  
        xmaxs.append(row['xmax'] / width)  
        ymins.append(row['ymin'] / height)  
        ymaxs.append(row['ymax'] / height)  
        classes_text.append(row['class'].encode('utf8'))  
        classes.append(class_text_to_int(row['class'], group.filename))
  
    tf_example = tf.train.Example(features=tf.train.Features(feature={  
        'image/height': dataset_util.int64_feature(height),  
        'image/width': dataset_util.int64_feature(width),  
        'image/filename': dataset_util.bytes_feature(filename),  
        'image/source_id': dataset_util.bytes_feature(filename),  
        'image/encoded': dataset_util.bytes_feature(encoded_jpg),  
        'image/format': dataset_util.bytes_feature(image_format),  
        'image/object/bbox/xmin': dataset_util.float_list_feature(xmins),  
        'image/object/bbox/xmax': dataset_util.float_list_feature(xmaxs),  
        'image/object/bbox/ymin': dataset_util.float_list_feature(ymins),  
        'image/object/bbox/ymax': dataset_util.float_list_feature(ymaxs),  
        'image/object/class/text': dataset_util.bytes_list_feature(classes_text),  
        'image/object/class/label': dataset_util.int64_list_feature(classes),  
    }))  
    return tf_example  
  
  
def main(_):  
    writer = tf.python_io.TFRecordWriter(FLAGS.output_path)  
    path = os.path.join(os.getcwd(), 'picture_data')  
    examples = pd.read_csv(FLAGS.csv_input)  
    grouped = split(examples, 'filename')  
    num=0  
    for group in grouped:  
        num+=1  
        tf_example = create_tf_example(group, path)  
        writer.write(tf_example.SerializeToString())  
        if(num%100==0):  #每完成100个转换，打印一次  
            print(num)  
  
    writer.close()  
    output_path = os.path.join(os.getcwd(), FLAGS.output_path)  
    print('Successfully created the TFRecords: {}'.format(output_path))  
  
  
if __name__ == '__main__':  
    tf.app.run()