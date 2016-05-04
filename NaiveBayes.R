mydata = read.table("/Users/meredithmargulies/Downloads/vec_output_R.txt", sep=" ")
install.packages("klaR")
install.packages("caret")

library("klaR")
library("caret")
mydata = mydata[,1:9]
colnames(mydata)[1] = "Rating"
mydata$Rating = as.factor(mydata$Rating)
colnames(mydata)[2] = "WordLength"
colnames(mydata)[3] = "AdjectivesPerWord"
colnames(mydata)[4] = "AdverbsPerWord"
colnames(mydata)[5] = "AverageNumberOfWordsPerItem"
colnames(mydata)[6] = "DescLength"
colnames(mydata)[7] = "AdjectivesPerDesc"
colnames(mydata)[8] = "AdverbsPerDesc"
colnames(mydata)[9] = "AverageNumberOfWordsPerDesc"


attach(mydata)
x = subset(mydata, select = -Rating)
y = Rating
del = train(x,y,'nb',trControl=trainControl(method='cv', number = 10))

del
prednew = predict(del, x)
tab = table(prednew, mydata$Rating)
tab
sum(tab[row(tab)==col(tab)])/sum(tab)

library(e1071)

svm_model = svm(x,y)
summary(svm_model)
svm_pred = predict(svm_model,x)
svm_con_matrix = table(svm_pred, y)
svm_con_matrix
sum(svm_con_matrix[row(svm_con_matrix)==col(svm_con_matrix)])/sum(svm_con_matrix)


