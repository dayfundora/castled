---
sidebar_position: 1
---

# On Local Workstation

---

:::caution
These steps have been tested on MacOS(Intel) and Ubuntu 20.04. Currently not supported on MacOS(M1).

Make sure that docker-compose is atleast 1.27.4 or else it can have issues with `platform` key in docker compose config yaml.
:::

## Setting up Castled

Deploying Castled locally is fairly straightforward.

1. Make sure you have [docker](https://docs.docker.com/get-docker/) and [docker compose](https://docs.docker.com/compose/install/) installed on your local machine
2. Follow the steps below for cloning the repo and running castled services.

   ```jsx title="Local Deployment"
   git clone https://github.com/castledio/castled.git
   cd castled
   docker-compose up
   ```

3. You will see the Castled ascii art showing up, once all the services are up.

   ![ascii art](/docs/static/img/screens/deploying/local/castled-ascii.png)

4. Castled UI should be available at [http://localhost:3000](http://localhost:3000). Happy castling!!!
