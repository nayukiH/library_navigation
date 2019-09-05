from PIL import Image
import os.path
import glob

annotations_test_dir = "annotations/test/"
Images_dir = "picture_data"
test_images_dir = "test_images"
i = 0
for xmlfile in os.listdir(annotations_test_dir):
    (filepath, tempfilename) = os.path.split(xmlfile)
    (shotname, extension) = os.path.splitext(tempfilename)
    xmlname = shotname
    for jpgfile in os.listdir(Images_dir):
        (filepath, tempfilename) = os.path.split(jpgfile)
        (jpgname, extension) = os.path.splitext(tempfilename)
        if pngname == xmlname:
             img = Image.open(Images_dir+"/" + jpgname + ".jpg")
             img.save(os.path.join(test_images_dir, os.path.basename(jpgfile)))
             print(jpgname)
             i += 1
print(i)