import difflib as dl
data = [line.removesuffix("\n").split(",") for line in open("./itemdb.csv").readlines()]
options = [line[1].lower() for line in data]
inp = input("Enter a query: ")

# get all options that start with inp
starts = [line for line in options if line.startswith(inp)]
print(starts)

matches = dl.get_close_matches(inp, options, 20, 0)

# add entries from matches to starts until it is of size 20
while len(starts) < 20:
    for match in matches:
        if match not in starts:
            starts.append(match)
            break

# print out options
print(str(len(starts)) + " matches:")
for i, match in enumerate(starts):
    if i == 5:
        break
    print(f"{i+1}. {match}")
selected = input("Select a match (-1 for more): ")

# print out more options
if selected == "-1":
    for i, match in enumerate(starts):
        print(f"{i+1}. {match}")
    selected = input("Select a match: ")

var = starts[int(selected) - 1]
print("Picked: ({}) {}".format(selected, var.capitalize()))

# print data for item
for line in data:
    if line[1].lower() == var:
        print(line)