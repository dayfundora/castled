---
sidebar_position: 2
---

# On AWS EC2 Instance

---

## 1. Creating EC2 Instance

Follow the below steps to create a new EC2 instance fairly quickly

1. Login to your AWS web console
2. Select EC2 service

   ![ec2 service](/docs/static/img/screens/deploying/ec2/ec2-service.png)

3. Select `Launch Instances` in navigation tab.

   ![ec2 launch](/docs/static/img/screens/deploying/ec2/ec2-launch-instances.png)

4. Select any `Amazon Linux 2 AMI` instance type

   ![ec2 instance type](/docs/static/img/screens/deploying/ec2/ec2-linux-ami.png)

   Choose at least a `t2.medium` instance type for the best experience.

   ![ec2 t2 instance](/docs/static/img/screens/deploying/ec2/ec2-t2-medium-instance.png)

5. `Next: Configure Instance Details`

   You can keep the default settings or configure to your needs.

6. `Next: Add Storage`
   Provision at least 25G of space for your instance.

   ![ec2 storage](/docs/static/img/screens/deploying/ec2/ec2-t2-storage.png)

7. `Next: Add Tags`

   You can add any key-value tags for your instance or skip it.

8. `Next: Configure Security Group`

   Note that your instances would be accessible from any IP address.
   We recommend that you update your security group rules to allow access from known IP addresses only.

   ![ec2 instance type](/docs/static/img/screens/deploying/ec2/ec2-security-group.png)

9. `Review and Launch`

   Create and download a new ssh key to login to your EC2 instance.
   You won't be able to ssh to your instance if you lost this key.

   ![ec2 ssh key](/docs/static/img/screens/deploying/ec2/ec2-ssh-key.png)

10. Now go back to `View Instances` and wait for your instance state to be running.

    ![ec2 instance status](/docs/static/img/screens/deploying/ec2/ec2-instance-status.png)

:::note
For more detailed information on EC2 setup please refer to the [AWS EC2 setup guide](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/EC2_GetStarted.html)
:::

---

## 2. Setting up Environment

Here we will setup a basic environment on the new EC2 instance to get Castled started.

1. Set readonly permission for your ssh private key you just downloaded during EC2 setup and use that to ssh into your EC2 instance.

   ```jsx title="ssh to EC2 instance"
   # In your local workstation
   # Replace ~/Downloads/castled-node-key.pem with the location of the downloaded private key.
   chmod 400 ~/Downloads/castled-node-key.pem

   # <public_ip_of_ec2> (Public IPv4 address) can be found once you select your instance in AWS web console.
   ssh -i ~/Downloads/castled-node-key.pem ec2_user@<public_ip_of_ec2>
   ```

2. Download Castled docker file

   ```jsx title="Download docker configs"
   # In the ec2 instance
   mkdir castled && cd castled
   wget https://raw.githubusercontent.com/castledio/castled/main/{.env,docker-compose.yaml,tools/bin/ec2_docker_install.sh}
   ```

3. Install docker and docker compose using our script.

   ```jsx title="Docker installation"
   # In the ec2 instance
   sh ec2_docker_install.sh
   logout
   ```

4. Make sure to logout so that the user group modification done by the script takes effect.

---

## 3. Running Castled

1. This is probably the easiest of all

   ```jsx title="Running Castled"
   # In your local workstation
   ssh -i ~/Downloads/castled-node-key.pem ec2_user@<public_ip_of_ec2>

    # You should be in the ec2 instance now

    cd castled
    docker-compose up -d
    logout
   ```

This will pull all the Castled docker images and runs them. Once the command is complete logout.

---

## 4. Setup Access

1. Setup a SSH tunnel for local port forwarding requests to the Castled webapp from your local machine.

   ```jsx title="Setting up ssh tunnel"
   # In your local workstation
   ssh -i  ~/Downloads/castled-node-key.pem -L 3000:localhost:3000 -N -f ec2-user@<public_ip_of_ec2>
   ```

You should be able to access Castled UI at [http://localhost:3000](http://localhost:3000) from your local workstation.
