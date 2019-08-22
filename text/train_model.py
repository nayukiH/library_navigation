batch_size = 
pic_classes = 1
epochs_run =

save_dir = "E:\Nproject\navigation\work"
res_dir = "E:\Nproject\navigation\result"
model_name = "convent_10"

# setup paths
import os

ckpt_dir = os.path.join(save_dir, "checkpoints")
if not os.path.isdir(ckpt_dir):
    os.makedirs(ckpt_dir)

model_path = os.path.join(res_dir, model_name + ".kerasave")
hist_path = os.path.join(res_dir, model_name + ".kerahist")

