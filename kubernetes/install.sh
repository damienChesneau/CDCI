#!/usr/bin/env bash
setenforce 0
export MY_IP=$(ip -4 addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}')
export PUBLIC_IP="10.157.56.158"
sed -i --follow-symlinks 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/sysconfig/selinux
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg
       https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF
yum install kubeadm docker -y
systemctl restart docker && systemctl enable docker
systemctl restart kubelet && systemctl enable kubelet

printf -v no_proxy1 '%s,' 10.244.0.{1..255};
printf -v no_proxy2 '%s,' 10.96.0.{1..255};
export no_proxy="$no_proxy1,$no_proxy";
export no_proxy="$no_proxy2,$no_proxy";
export no_proxy="$no_proxy,$MY_IP"

kubeadm init --pod-network-cidr=10.244.0.0/16 --apiserver-advertise-address $MY_IP
kubectl taint nodes $(hostname) node-role.kubernetes.io/master:NoSchedule-

mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
#---Network flannel  --pod-network-cidr=10.244.0.0/16 is dependant
sysctl net.bridge.bridge-nf-call-iptables=1
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/a70459be0084506e4ec919aa1c114638878db11b/Documentation/kube-flannel.yml

kubectl get nodes

kubectl apply -f ./kubernetes-dashboard-noauth.yaml
cat <<EOF > ./auth-dashboard.yaml
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: kubernetes-dashboard
  labels:
    k8s-app: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: kubernetes-dashboard
  namespace: kube-system
EOF
kubectl apply -f ./auth-dashboard.yaml
kubectl proxy --accept-hosts='.*' --accept-paths='.*' --address=$MY_IP &

kubectl expose deployment nginx-deployment --port=80 --target-port=80 --type=LoadBalancer --external-ip=$PUBLIC_IP


