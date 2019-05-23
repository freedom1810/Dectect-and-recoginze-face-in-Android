#include <jni.h>
#include <string>
#include <android/log.h>
#include <dlib/image_io.h>
#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/dnn.h>
#include <dlib/clustering.h>
#include <time.h>
#include <stdio.h>
#include "nhan_dien.h"
#define  LOG_TAG    "someTag"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)



using namespace dlib;
using namespace std;

inline  std::vector<int > getfacerect(const std::vector<int>img,int height,int width)
{
    static  dlib::frontal_face_detector face_detector = dlib::get_frontal_face_detector();

    dlib::array2d<unsigned char>image;
    image.set_size(height,width);

    for (int i = 0; i < height; i++)
    {
        for(int j=0;j<width;j++)
        {
            int clr = img[i*width+j];
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;
            unsigned char gray=red*0.299+green*0.587+blue*0.114;

            image[i][j]=gray;
        }
    }

    std::vector<dlib::rectangle> dets= face_detector(image);

    std::vector<matrix<float, 0, 1>> ad = detect(image);
    std::vector<int>rect;

    rect.push_back(dets.size());

    for(int i = 0; i < dets.size(); i ++){
        rect.push_back(dets[i].left());
        rect.push_back(dets[i].top());
        rect.push_back(dets[i].width());
        rect.push_back(dets[i].height());
    }
    return rect;

}

extern "C" JNIEXPORT jintArray JNICALL Java_com_example_project2_DlibNative_detecFace(JNIEnv *env, jclass clazz, jintArray image_data, jint image_width, jint image_height) {


    LOGD("this time start %f", (float)clock()/CLOCKS_PER_SEC);
    std::vector<int> image_datacpp(image_height * image_width);
    jsize len = env->GetArrayLength(image_data);
    jint *body = env->GetIntArrayElements(image_data, 0);

    for (jsize i=0;i<len;i++){
        image_datacpp[i]=(int)body[i];
    }


    std::vector<int>rect=getfacerect(image_datacpp,image_height,image_width);


    jintArray result =env->NewIntArray(rect.size());
    env->SetIntArrayRegion(result, 0, rect.size(), &rect[0]);
    LOGD("this time start %f", (float)clock()/CLOCKS_PER_SEC);
    return result;
}
