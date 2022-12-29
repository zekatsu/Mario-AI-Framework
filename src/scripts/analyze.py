# %%
import numpy as np
import pandas as pd
# %%
df = pd.read_csv("../../data/gameEvents.csv", header=None)
df.head()
# %%
df[2].unique()
# %%
