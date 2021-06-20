from flask import Flask, request, jsonify
from flask_cors import CORS
from requests import get
import json

app = Flask(__name__)
CORS(app)

workflows_infos = dict()
error_dic = dict()

@app.route("/workflows", methods=["GET"])
def get_workflows():
    workflows = dict()

    page_number = request.args.get('page_number')

    #response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows?page={page_number}")
#
    #if response.status_code != 200:
    #    print("Error on GitHub's response")
    #    return error_dic
    #response_json = response.json()




    f = open('workflows_test.json')
    response_json = json.load(f)
    print(json.dumps(response_json, indent=4))







    workflows["workflows_number"] = response_json["total_count"]

    if "workflows" in response_json:
        workflows_list = response_json["workflows"]

    workflows["workflows"] = list()

    for workflow in workflows_list:
        w = dict()

        w["id"] = workflow["id"] if ("id" in workflow) else None
        w["name"] = workflow["name"] if ("name" in workflow) else None
        w["state"] = workflow["state"] if ("state" in workflow) else None
        w["created_at"] = workflow["created_at"] if ("created_at" in workflow) else None
        w["updated_at"] = workflow["updated_at"] if ("updated_at" in workflow) else None

        workflows["workflows"].append(w)

    return workflows

@app.route("/workflow-runs", methods=["POST"])
def get_workflow_info():
    workflow = dict()
    workflow_info = dict()

    data = request.get_json()

    workflow_name = data.get("workflow")
    page_number = data.get("page_number")

    #response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows/{workflow_name}/runs?per_page=5&page={page_number}")
#
    #if response.status_code != 200: 
    #    print("Error on GitHub's response")
    #    return error_dic
#
    #response_json = response.json()
    f = open('workflow_runs_test.json')
    response_json = json.load(f)
    print(json.dumps(response_json, indent=4))
    
    workflow["runs"] = response_json["total_count"]

    if "workflow_runs" in response_json:
        workflow_runs_list = response_json["workflow_runs"]

    workflow["data"] = list()

    for run in response_json["workflow_runs"]:
        r = dict()

        r["id"] = run["id"] if ("id" in run) else None
        r["name"] = run["name"] if ("name" in run) else None
        r["event"] = run["event"] if ("event" in run) else None
        r["status"] = run["status"] if ("status" in run) else None
        r["conclusion"] = run["conclusion"] if ("conclusion" in run) else None
        r["pull_requests"] = run["pull_requests"] if ("pull_requests" in run) else None
        r["created_at"] = run["created_at"] if ("created_at" in run) else None
        r["updated_at"] = run["updated_at"] if ("updated_at" in run) else None
        r["head_commit"] = run["head_commit"] if ("head_commit" in run) else None

        run_id = run["id"]

        #response2 = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/runs/{run_id}/timing")
#
        #if response2.status_code != 200: 
        #    print("Error on GitHub's response")
        #    return error_dic
#
        #response_json2 = response2.json()

        f2 = open('workflow_runs_954738441_timing_test.json')
        response_json2 = json.load(f2)

        r["duration"] = response_json2["run_duration_ms"]

        workflow["data"].append(r)

    workflows_infos[workflow_name] = workflow
    
    return workflows_infos

if __name__ == "__main__":
    app.run(host='0.0.0.0')
