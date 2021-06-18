from flask import Flask, request, jsonify
from requests import get

app = Flask(__name__)

workflows_infos = dict()
error_dic = dict()

@app.route("/workflows", methods=["GET"])
def get_workflows():
    workflows = dict()

    data = request.get_json()

    page_number = data.get("page_number")

    response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows?page={page_number}")

    if response.status_code != 200:
        print("Error on GitHub's response")
        return error_dic

    response_json = response.json()

    workflows["workflows_number"] = response_json["total_count"]

    workflows["workflows"] = list()

    for workflow in response_json["workflows"]:
        w = dict()

        w["id"] = workflow["id"]
        w["name"] = workflow["name"]
        w["state"] = workflow["state"]
        w["created_at"] = workflow["created_at"]
        w["updated_at"] = workflow["updated_at"]

        workflows["workflows"].append(w)

    return workflows

@app.route("/workflow-runs", methods=["POST"])
def get_workflow_info():
    workflow = dict()
    workflow_info = dict()

    data = request.get_json()

    workflow_name = data.get("workflow")
    page_number = data.get("page_number")

    response = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/workflows/{workflow_name}/runs?per_page=5&page={page_number}")

    if response.status_code != 200: 
        print("Error on GitHub's response")
        return error_dic

    response_json = response.json()
    
    workflow["runs"] = response_json["total_count"]
    workflow["data"] = list()

    for run in response_json["workflow_runs"]:
        r = dict()

        r["id"] = run["id"]
        r["name"] = run["name"]
        r["event"] = run["event"]
        r["status"] = run["status"]
        r["conclusion"] = run["conclusion"]
        r["pull_requests"] = run["pull_requests"]
        r["created_at"] = run["created_at"]
        r["updated_at"] = run["updated_at"]
        r["head_commit"] = run["head_commit"]

        run_id = run["id"]

        response2 = get(url=f"https://api.github.com/repos/eduardosantoshf/t-tracker/actions/runs/{run_id}/timing")

        if response2.status_code != 200: 
            print("Error on GitHub's response")
            return error_dic

        response_json2 = response2.json()

        r["duration"] = response_json2["run_duration_ms"]

        workflow["data"].append(r)

    workflows_infos[workflow_name] = workflow
    
    return workflows_infos

if __name__ == "__main__":
    app.run(host='0.0.0.0')
