"""
Addon which update allow hosts list

Usage:

    mitmproxy -s allow-hosts.py

    and then send a HTTP request to trigger the shutdown:
    curl --proxy localhost:8089 http://updateallowhosts.com/hosts
"""
import logging

from mitmproxy import ctx
from mitmproxy import http

def load(l):
    logging.info("Load allow_hosts script")

def request(flow: http.HTTPFlow) -> None:
    if "http://updateallowhosts.com" in flow.request.pretty_url:
        rawhosts = flow.request.pretty_url.replace('http://updateallowhosts.com/', '')
        hosts = rawhosts.replace('_', '|')
        logging.info("Old allow_hosts")
        logging.info(ctx.options.allow_hosts)
        ctx.options.allow_hosts = [hosts]
        logging.info("New allow_hosts")
        logging.info(ctx.options.allow_hosts)
        flow.kill()
