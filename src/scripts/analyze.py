# %%
import numpy as np
import pandas as pd
# %%
df = pd.read_csv("../../data/gameEvents.csv", header=None)
df.head()
# %%
df[2].unique()
# %%
df[1].unique()
# %%
print(df[df[2] == 22][0])
# %%
