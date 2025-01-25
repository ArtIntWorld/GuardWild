
def MaxSumAreaRectangle(a,n):
    a.sort(reverse=True)
    sum_area=0
    l=0
    i=0
    flag=0

    while i<n:
        if (a[i]==a[i+1] or (a[i]-1)==a[i+1]) and flag==0:
            l=a[i-1]
            flag=1
            i+=1

        elif (a[i]==a[i+1] or (a[i]-1)==a[i+1]) and flag==1:
            sum_area+=l*a[i+1]
            flag=0
            i+=1
    return sum_area


