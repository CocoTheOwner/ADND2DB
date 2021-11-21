# ADND2DB
A (set of) database(s) for AD&amp;D version 2 based on the [Players' Handbook](./DD_Players_Handbook) (Chapter 6: Armor and Equipment) and [Charles W. Plemons III's Weapons List](https://people.wku.edu/charles.plemons/ad&d/misc/arms.html) (Credits for that data go to him)

# Retrieval tool for Dungeon Masters
Use [Getitem](./getitem.py) for item retrieval from text (does deep matching & always finds what you need, if it's in the database)

# Future Ideas
- Categories (for players & mules)
  - Armor
  - Weapons
  - Magic Items (if present)
  - Misc
- Sorted alphabetically
- Showing weight, value, on-hover description (?)
- Random item remover (from mule) or 'mule decider' and 'mule inserter' (item value -> weight)
- Easy selling
- Easy buying (i.e. adding)
- Simplify access to money as much as possible
- Bulk adding/removing
- 'loot' category (in mules)
- Best-before date
- Quality (-1 = description, 0 = 0%, 5 = 100%, 10 = 200%)
- Logging
- 'lootboxes' - preprepared loot
  - Inventory merging
  - Loot marker
- Bulk selling with specific modifiers
